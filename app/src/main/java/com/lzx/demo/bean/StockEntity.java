package com.lzx.demo.bean;

import java.util.List;

/**
 * Created by Oubowu on 2016/7/27 12:59.
 */
public class StockEntity {

    // 振幅榜
    public List<StockInfo> amplitude_list;

    // 跌幅榜
    public List<StockInfo> down_list;

    // 换手率
    public List<StockInfo> change_list;

    // 涨幅榜
    public List<StockInfo> increase_list;

    public static class StockInfo {
        public double rate;
        public String current_price;
        public String stock_code;
        public String stock_name;
    }

}
