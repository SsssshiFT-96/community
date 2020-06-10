package com.stndorm.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来封存页码信息
 */
@Data
public class PaginationDTO<T> {
    private List<T> data;

    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showEndPage;
    private boolean showNext;

    private Integer page;

    private Integer totalPage;

    private List<Integer> pages = new ArrayList<>();

    public void setPagination(Integer totalPage, Integer page) {
        this.totalPage = totalPage;
        this.page = page;
        /*
        设置底下页码显示多少数字
        先将当前页存入集合中，因为要显示出当前的前3页和后3页
        所以拿当前页进行for循环，分别对1,2,3进行加减判断是否存在
        若存在，在之前的页数就加到集合前面，在后面的页数就加到集合后面
         */
        pages.add(page);
        for(int i = 1; i <= 3; i++){
            if(page - i > 0){
                pages.add(0,page - i);
            }
            if(page + i <= totalPage){
                pages.add(page + i);
            }
        }

        //是否展示跳转到上一页
        if(page == 1){
            showPrevious = false;
        }else{
            showPrevious = true;
        }
        //是否展示跳转到下一页
        if(page == totalPage){
            showNext = false;
        }else{
            showNext = true;
        }

        //是否展示跳转到首页，即当页数码没有显示第一页时展示
        if(!pages.contains(1)){
            showFirstPage = true;
        }else{
            showFirstPage = false;
        }
        //是否展示跳转到尾页
        if(!pages.contains(totalPage)){
            showEndPage = true;
        }else{
            showEndPage = false;
        }
    }
}
