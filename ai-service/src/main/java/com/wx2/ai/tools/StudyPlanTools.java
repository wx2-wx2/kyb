package com.wx2.ai.tools;

import com.wx2.api.client.CartFeignClient;
import com.wx2.api.client.CollegeFeignClient;
import com.wx2.api.client.ProductFeignClient;
import com.wx2.common.model.PageData;
import com.wx2.common.model.query.CartNumQuery;
import com.wx2.common.model.query.CollegePageQuery;
import com.wx2.common.model.query.ProductPageQuery;
import com.wx2.common.model.vo.CollegeVO;
import com.wx2.common.model.vo.CollegeWithMajorVO;
import com.wx2.common.model.vo.ProductVO;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StudyPlanTools {

    private final CollegeFeignClient collegeFeignClient;
    private final ProductFeignClient productFeignClient;
    private final CartFeignClient cartFeignClient;

    @Tool(description = "根据条件查询院校及其专业")
    public List<CollegeWithMajorVO> getCollege(@ToolParam(description = "院校省份") String province,
                                               @ToolParam(description = "院校城市") String city,
                                               @ToolParam(description = "院校层次") Integer level,
                                               @ToolParam(description = "院校类型") Integer type,
                                               @ToolParam(description = "专业名称") String majorName,
                                               @ToolParam(description = "学位类型") Integer degreeType) {
        CollegePageQuery collegePageQuery = new CollegePageQuery();
        collegePageQuery.setProvince(province);
        collegePageQuery.setCity(city);
        collegePageQuery.setLevel(level);
        collegePageQuery.setType(type);
        collegePageQuery.setMajorName(majorName);
        collegePageQuery.setDegreeType(degreeType);
        PageData<CollegeVO> collegePage = collegeFeignClient.getCollegeByPage(collegePageQuery);
        return collegePage.getRecords().stream()
                .map(college -> collegeFeignClient.getCollegeDetailInfoById(college.getId()))
                .collect(Collectors.toList());
    }

    @Tool(description = "根据条件查询商品")
    public List<ProductVO> getProduct(@ToolParam(description = "商品关键词") String keyword,
                                      @ToolParam(description = "商品类目") Integer category,
                                      @ToolParam(description = "商品学科") String subject,
                                      @ToolParam(description = "商品最低价") BigDecimal minPrice,
                                      @ToolParam(description = "商品最高价") BigDecimal maxPrice) {
        ProductPageQuery productPageQuery = new ProductPageQuery();
        productPageQuery.setKeyword(keyword);
        productPageQuery.setCategory(category);
        productPageQuery.setSubject(subject);
        productPageQuery.setMinPrice(minPrice);
        productPageQuery.setMaxPrice(maxPrice);
        PageData<ProductVO> productPage = productFeignClient.getProductByPage(productPageQuery);
        return productPage.getRecords();
    }

    @Tool(description = "将符合条件的商品加入购物车")
    public void addToCart(@ToolParam(description = "商品id") Long productId,
                          @ToolParam(description = "商品数量") Integer num) {
        CartNumQuery cartNumQuery = new CartNumQuery();
        cartNumQuery.setProductId(productId);
        cartNumQuery.setNum(num);
        cartFeignClient.addToCart(cartNumQuery);
    }
}
