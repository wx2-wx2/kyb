package com.wx2.common.model;

import com.wx2.common.model.query.PageQuery;
import lombok.Data;

import java.util.List;

@Data
public class PageData<T> {
    // 当前页数据列表
    private List<T> records;
    // 总记录数
    private long total;
    // 当前页码
    private int page;
    // 每页大小
    private int size;
    // 总页数
    private long totalPages;
    // 排序字段（与请求参数对应）
    private String sortField;
    // 排序方向
    private String sortDirection;

    /**
     * 构造函数：通过分页请求和查询结果构建分页数据
     */
    public PageData(List<T> records, long total, PageQuery pageQuery) {
        this.records = records;
        this.total = total;
        this.page = pageQuery.getPage();
        this.size = pageQuery.getSize();
        this.sortField = pageQuery.getSortField();
        this.sortDirection = pageQuery.getSortDirection();
        this.totalPages = calculateTotalPages(total, pageQuery.getSize());
    }

    /**
     * 计算总页数
     */
    private long calculateTotalPages(long total, int size) {
        if (total <= 0) {
            return 0;
        }
        // 向上取整计算总页数
        return (total + size - 1) / size;
    }

    /**
     * 判断是否有下一页
     */
    public boolean hasNext() {
        return page < totalPages;
    }

    /**
     * 判断是否有上一页
     */
    public boolean hasPrevious() {
        return page > 1;
    }
}
