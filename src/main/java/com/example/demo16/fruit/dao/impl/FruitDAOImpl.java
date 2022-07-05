package com.example.demo16.fruit.dao.impl;
import com.example.demo16.fruit.dao.FruitDAO;
import com.example.demo16.fruit.pojo.Fruit;
import com.example.demo16.myssm.basedao.BaseDAO;
import java.util.List;

public class FruitDAOImpl extends BaseDAO<Fruit> implements FruitDAO {
    @Override
    public List<Fruit> getFruitList( String keyword ,int PageNum) {
        return super.executeQuery("select * from t_fruit where fname like ? or remark like ? limit ? ,5","%"+keyword+"%","%"+keyword+"%",(PageNum - 1) * 5);
    }
    @Override
    public Fruit getFruitByFid(int fid) {
        return super.load("select * from t_fruit where fid = ?",fid);
    }
    @Override
    public void UpdataFruit(Fruit fruit) {
        String sql = "update t_fruit set fname = ?,price = ?,fcount = ?,remark = ? where fid = ?";
        super.executeUpdate(sql,fruit.getFname(),fruit.getPrice(),fruit.getFcount(),fruit.getRemark(),fruit.getFid());
    }
    @Override
    public void delFruit(int fid) {
        super.executeUpdate("delete from t_fruit where fid = ?",fid);
    }
    @Override
    public void addFruit(Fruit fruit) {
        String sql = "insert into t_fruit values(?,?,?,?,?)";
        super.executeUpdate(sql,fruit.getFid(),fruit.getFname(),fruit.getPrice(),fruit.getFcount(),fruit.getRemark());
    }

    @Override
    public Long getFruitCount(String keyword) {
        return (Long) super.executeComplexQuery("select count(*) from t_fruit where fname like ? or remark like ?","%"+keyword+"%","%"+keyword+"%")[0];
    }
}
