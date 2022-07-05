package com.example.demo16.fruit.dao;

import com.example.demo16.fruit.pojo.Fruit;

import java.util.List;

public interface FruitDAO {
    //获取所有的库存列表信息
    List<Fruit> getFruitList( String keyword,int PageNum);
    // 根据fid获取水果库存信息
    Fruit getFruitByFid(int fid);
    // 修改指定的库存记录
    void UpdataFruit(Fruit fruit);
    // 根据fid删除记录
    void delFruit(int fid);
    void addFruit(Fruit fruit);
    // 查询总记录条数的方法
    Long getFruitCount(String keyword);
}
