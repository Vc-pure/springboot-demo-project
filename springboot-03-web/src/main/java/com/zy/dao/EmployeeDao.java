package com.zy.dao;

import com.zy.pojo.Department;
import com.zy.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//员工Dao
@Repository
public class EmployeeDao {

    //模拟数据库中的数据
    private static Map<Integer, Employee> employees = null;
    //员工有所属的部门
    @Autowired
    private DepartmentDao departmentDao;

    static {
        employees = new HashMap<Integer, Employee>();  //创建一个部门表

        employees.put(1001,new Employee(1001,"AA","111111111@qq.com",0,new Department(101,"教学部")));
        employees.put(1002,new Employee(1002,"BB","222222222@qq.com",1,new Department(102,"市场部")));
        employees.put(1003,new Employee(1003,"CC","333333333@qq.com",0,new Department(103,"教研部")));
        employees.put(1004,new Employee(1004,"DD","444444444@qq.com",1,new Department(104,"运营部")));
        employees.put(1005,new Employee(1005,"EE","555555555@qq.com",0,new Department(105,"后勤部")));

    }
    //主键自增
    private static Integer initId =1006;
    //增加一个员工
    public void save(Employee employee){
        if(employee.getId()==null){
            employee.setId(initId++);
        }
        employee.setDepartment(departmentDao.getDepartment(employee.getDepartment().getId()));

        employees.put(employee.getId(),employee);
    }

    //如果连以上以及以下的能力都没有， 就把这个代码敲一百遍，就能对数据库精通了

    //查询全部员工信息
    public Collection<Employee> getAll(){
        return employees.values();
    }

    //通过id查询员工
    //一般规范一点都是用Integer，因为如果用int的话会去自动装箱和自动拆箱，涉及到时间片问题，会影响效率
    public Employee getEmployeeById(Integer id){
        return employees.get(id);
    }

    //通过id删除员工
    public void delete(Integer id){
        employees.remove(id);
    }

}


/*
    如果想把这些数据对标到数据库，只需要把数据改一下就好了，增删改查写成一个mapper里面对应的sql
 */
