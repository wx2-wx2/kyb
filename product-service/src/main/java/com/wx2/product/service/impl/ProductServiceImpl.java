package com.wx2.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.NumberRangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx2.common.error.ProductError;
import com.wx2.common.exception.BizException;
import com.wx2.common.model.PageData;
import com.wx2.common.model.dto.OrderDetailDTO;
import com.wx2.common.model.query.StatusQuery;
import com.wx2.product.enums.ProductStatus;
import com.wx2.product.mapper.ProductMapper;
import com.wx2.product.model.entity.Product;
import com.wx2.common.model.query.ProductPageQuery;
import com.wx2.common.model.vo.ProductVO;
import com.wx2.product.model.query.ProductQuery;
import com.wx2.product.model.query.ProductSoldQuery;
import com.wx2.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.wx2.common.constant.MqConstant.*;
import static com.wx2.common.constant.RedisConstant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ProductMapper productMapper;
    private final ElasticsearchClient elasticsearchClient;
    private final RabbitTemplate rabbitTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public ProductVO getProductById(Long productId) {
        // 查询并检验商品是否为空
        ProductVO productVO = getWithMutex(productId);
        if (ObjectUtil.isNull(productVO)) {
            throw new BizException(ProductError.NOT_EXIST);
        }

        return productVO;
    }

    @Override
    public List<ProductVO> getProductByIds(List<Long> productIds) {
        // 批量查询商品
        List<Product> productList = listByIds(productIds);

        return productList.stream()
                .map(product -> {
                    ProductVO vo = new ProductVO();
                    BeanUtil.copyProperties(product, vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PageData<ProductVO> getProductByPage(ProductPageQuery request) {
        try {
            // 从ES分页查询商品
            return getByEs(request);
        } catch (Exception e) {
            // 从数据库分页查询商品
            return getByDb(request);
        }
    }

    @Override
    @Transactional
    public void addProduct(ProductQuery request) {
        // 新增商品
        Product product = BeanUtil.copyProperties(request, Product.class);
        save(product);
        product = getById(product.getId());
        // 向消息队列发送ES商品同步新增消息
        try {
            rabbitTemplate.convertAndSend(ES_EXCHANGE, ES_PRODUCT_UPDATE_KEY, product);
        } catch (Exception e) {
            log.error("发送ES商品同步新增消息失败", e);
        }
    }

    @Override
    @Transactional
    public void updateProductById(ProductQuery request) {
        // 更新商品
        updateById(BeanUtil.copyProperties(request, Product.class));
        Product product = getById(request.getId());
        // 向消息队列发送ES商品同步更新消息
        try {
            rabbitTemplate.convertAndSend(ES_EXCHANGE, ES_PRODUCT_UPDATE_KEY, product);
        } catch (Exception e) {
            log.error("发送ES商品同步更新消息失败，id：{}", request.getId(), e);
        }
    }

    @Override
    @Transactional
    public void deleteProductById(Long productId) {
        // 删除商品
        removeById(productId);
        // 向消息队列发送ES商品同步删除消息
        try {
            rabbitTemplate.convertAndSend(ES_EXCHANGE, ES_PRODUCT_DELETE_KEY, productId.toString());
        } catch (Exception e) {
            log.error("发送ES删除消息失败，id：{}", productId, e);
            throw new RuntimeException("删除同步失败");
        }
    }

    @Override
    @Transactional
    public void updateProductStatus(StatusQuery request) {
        // 更新商品状态
        Product product = getById(request.getId());
        product.setStatus(request.getStatus());
        updateById(product);
        // 向消息队列发送ES商品同步更新消息
        try {
            rabbitTemplate.convertAndSend(ES_EXCHANGE, ES_PRODUCT_UPDATE_KEY, product);
        } catch (Exception e) {
            log.error("发送ES商品同步更新消息失败，id：{}", request.getId(), e);
        }
    }

    @Override
    @Transactional
    public void updateProductStock(OrderDetailDTO dto) {
        Long productId = dto.getProductId();
        Integer num = dto.getNum();
        Product product = getById(productId);
        // 检验商品是否存在
        if (ObjectUtil.isNull(product)) {
            throw new BizException(ProductError.NOT_EXIST);
        }
        // 检验商品库存更新后是否小于0
        if (product.getStock() + num < 0) {
            throw new BizException(ProductError.STOCK_INSUFFICIENT);
        }
        // 更新商品库存
        product.setStock(product.getStock() + num);
        updateById(product);
        // 向消息队列发送ES商品同步更新消息
        try {
            rabbitTemplate.convertAndSend(ES_EXCHANGE, ES_PRODUCT_UPDATE_KEY, product);
        } catch (Exception e) {
            log.error("发送ES商品同步更新消息失败，id：{}", productId, e);
        }
    }

    @Override
    @Transactional
    public void updateProductSold(ProductSoldQuery query) {
        Long productId = query.getProductId();
        Integer sold = query.getSold();
        Product product = getById(productId);
        // 检验商品是否存在
        if (ObjectUtil.isNull(product)) {
            throw new BizException(ProductError.NOT_EXIST);
        }
        // 更新商品销量
        product.setSold(product.getSold() + sold);
        updateById(product);
        // 向消息队列发送ES商品同步更新消息
        try {
            rabbitTemplate.convertAndSend(ES_EXCHANGE, ES_PRODUCT_UPDATE_KEY, product);
        } catch (Exception e) {
            log.error("发送ES商品同步更新消息失败，id：{}", productId, e);
        }
    }

    private PageData<ProductVO> getByEs(ProductPageQuery query) throws IOException {
        // 创建ES查询请求构建器
        SearchRequest.Builder requestBuilder = new SearchRequest.Builder().index("product");
        // 创建ES查询请求构建器
        requestBuilder.query(q -> q.bool(b -> {
            // 关键词匹配
            if (StrUtil.isNotBlank(query.getKeyword())) {
                b.should(s -> s.match(m -> m.field("name").query(query.getKeyword())));
                b.should(s -> s.match(m -> m.field("description").query(query.getKeyword())));
                b.minimumShouldMatch("1");
            }
            // 类目筛选
            if (ObjectUtil.isNotNull(query.getCategory())) {
                b.filter(f -> f.term(t -> t.field("category").value(query.getCategory())));
            }
            // 学科筛选
            if (StrUtil.isNotBlank(query.getSubject())) {
                b.filter(f -> f.term(t -> t.field("subject").value(query.getSubject())));
            }
            // 状态筛选
            b.filter(f -> f.term(t -> t.field("status").value(ProductStatus.NORMAL.getCode())));
            // 价格范围筛选
            if (ObjectUtil.isNotNull(query.getMinPrice()) || ObjectUtil.isNotNull(query.getMaxPrice())) {
                NumberRangeQuery priceRangeQuery = NumberRangeQuery.of(n -> {
                    n.field("price");
                    if (ObjectUtil.isNotNull(query.getMinPrice())) {
                        n.gte(query.getMinPrice().doubleValue());
                    }
                    if (ObjectUtil.isNotNull(query.getMaxPrice())) {
                        n.lte(query.getMaxPrice().doubleValue());
                    }
                    return n;
                });

                RangeQuery priceRange = RangeQuery.of(r -> r.number(priceRangeQuery));

                b.filter(f -> f.range(priceRange));
            }

            return b;

        }));
        // 构建排序条件
        if (StrUtil.isNotBlank(query.getSortField())) {
            SortOrder order = "asc".equalsIgnoreCase(query.getSortDirection())
                    ? SortOrder.Asc
                    : SortOrder.Desc;
            requestBuilder.sort(s -> s.field(f -> f.field(query.getSortField()).order(order)));
        } else {
            requestBuilder.sort(s -> s.score(sc -> sc.order(SortOrder.Desc)));
        }
        // 构建分页条件
        requestBuilder.from(query.getOffset()).size(query.getSize());
        // 执行ES查询
        SearchResponse<Product> response = elasticsearchClient.search(
                requestBuilder.build(),
                Product.class
        );
        // 将查询结果封装成为VO列表
        List<ProductVO> productVOList = response.hits().hits().stream()
                .map(hit -> {
                    Product product = hit.source();
                    ProductVO vo = new ProductVO();
                    BeanUtil.copyProperties(product, vo);
                    return vo;
                })
                .collect(Collectors.toList());
        // 计算总记录数
        long total = 0;
        TotalHits totalHits = response.hits().total();
        if (totalHits != null && totalHits.relation() == TotalHitsRelation.Eq) {
            total = totalHits.value();
        }
        // 构建并返回分页数据对象
        return new PageData<>(productVOList, total, query);
    }

    private PageData<ProductVO> getByDb(ProductPageQuery query) {
        // 查询总记录数
        long total = productMapper.countByQuery(query);
        // 查询商品信息
        List<Product> productList = productMapper.selectByQuery(query);

        List<ProductVO> voList = productList.stream()
                .map(product -> {
                    ProductVO vo = new ProductVO();
                    BeanUtil.copyProperties(product, vo);
                    return vo;
                })
                .collect(Collectors.toList());
        // 构建并返回分页数据对象
        return new PageData<>(voList, total, query);
    }

    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }

    private ProductVO getWithMutex(Long productId) {
        String cacheKey = CACHE_PRODUCT_PREFIX + productId;
        // 从redis查询题目
        String productJson = stringRedisTemplate.opsForValue().get(cacheKey);
        // 缓存命中直接返回
        if (StrUtil.isNotBlank(productJson)) {
            return JSONUtil.toBean(productJson, ProductVO.class);
        }
        if (productJson != null) {
            return null;
        }
        // 重建缓存
        String lockKey = PRODUCT_LOCK_PREFIX + productId;
        ProductVO productVO = new ProductVO();

        try {
            // 尝试获取互斥锁
            boolean isLock = tryLock(lockKey);
            // 未获取到锁，50m后重试
            if (!isLock) {
                Thread.sleep(50);
                return getWithMutex(productId);
            }
            // 从数据库查询商品
            Product product = getById(productId);
            // 题目不存在，缓存空值
            if (ObjectUtil.isNull(product)) {
                stringRedisTemplate.opsForValue().set(cacheKey, "", NULL_EXPIRATION_MINUTES, TimeUnit.MINUTES);
            }
            BeanUtil.copyProperties(product, productVO);
            // 存入redis
            stringRedisTemplate.opsForValue().set(cacheKey, JSONUtil.toJsonStr(productVO), EXPIRATION_MINUTES, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 释放互斥锁
            unlock(lockKey);
        }

        return productVO;
    }
}
