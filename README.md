# 第九章 员工管理系统（springboot的一个小系统）

## 相关知识补充：

### lombok

#### 1、介绍

[lombok](https://so.csdn.net/so/search?q=lombok&spm=1001.2101.3001.7020)是一个插件，用途是使用注解给你类里面的字段，自动的加上属性，构造器，ToString方法，Equals方法等等,比较方便的一点是，你在更改字段的时候，lombok会立即发生改变以保持和你代码的一致性。

#### 2、常用的 lombok 注解介绍

@Getter 加在类上，可以自动生成参数的getter方法。

@Setter 加在类上，可以自动生成参数的setter方法

@ToString 加在类上，调用toString()方法，可以输出[实体类](https://so.csdn.net/so/search?q=实体类&spm=1001.2101.3001.7020)中所有属性的值

@RequiredArgsConstructor会生成一个包含常量，和标识了NotNull的变量的构造方法。生成的构造方法是私有的private。这个我用的很少。

@EqualsAndHashCode
(1).它会生成[equals](https://so.csdn.net/so/search?q=equals&spm=1001.2101.3001.7020)和hashCode方法
(2).默认使用非静态的属性
(3).可以通过exclude参数排除不需要生成的属性
(4).可以通过of参数来指定需要生成的属性
(5).它默认不调用父类的方法，只使用本类定义的属性进行操作，可以使用callSuper=true来解决，会在@Data中进行讲解。

@Data这个是非常常用的注解，这个注解其实是五个注解的合体：(提供类的get、set、EqualsAndHashCode、toString方法)

@NoArgsConstructor生成一个无参数的构造方法。

@AllArgsConstructor生成一个包含所有变量的构造方法。

#### 3.idea安装lombok插件

![img](https://img-blog.csdnimg.cn/228ea0ac4fbd4037a2a964445538ca32.jpg)

![img](https://img-blog.csdnimg.cn/75534b22921b47c48497b9903754ee6c.jpg?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBAQmx1ZS1TZWE=,size_20,color_FFFFFF,t_70,g_se,x_16)

![img](https://img-blog.csdnimg.cn/97a07f2fccc8469bb86860f23980b3c9.jpg?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBAQmx1ZS1TZWE=,size_20,color_FFFFFF,t_70,g_se,x_16)

**注意：安装完后一定要重启idea**

#### 4.lombok使用

（1）引入依赖

```xml
<!--引入lombok依赖-->
<dependency>
  <groupId>org.projectlombok</groupId>
  <artifactId>lombok</artifactId>
</dependency>
```

（2）在Employee类上添加lombok注解

```java
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Employee {
 private Long empId;
 private String name;
 private String empGender;
 private Integer age;
 private String email;
}
/*
//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
@Data //常用注解：
@AllArgsConstructor //全参数构造器
@NoArgsConstructor //无参数构造器
@TableName("employee") //能够和底层的数据库表明进行对应：
*/ 


```



### Fragment Expressions

Fragment Expressions: `~{...}`：片段引用表达式 （可以提取出公共的部分写到一个新的文件里）

比如在前端页面顶部导航栏或者侧边导航栏中通过 th:fragment="自定义名称",把这个导航栏抽取出来定义为刚才自定义名称的部分

然后在另一个前端页面通过片段引用表达式进行引用。

(该例中<div th:insert="~{dashboard::topbar}"></div>  就是片段引用表达式，dashboard表示引用片段的出处，topbar表示引用的片段) （insert可以换成replace）

```html
dashboard.html:
<!--	定义顶部导航栏-->
<nav class="navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0" th:fragment="topbar">

list.html:
<div th:insert="~{dashboard::topbar}"></div>  
```

当然也可把重复部分如顶部导航栏和侧边导航栏抽取出来，放到一个新的html页面，如commons/commons.html，这样的话引用的代码就变成了dashboard.html和list.html都要写且写的形式如下

```html
...
<div th:replace="~{commons/commons::topbar}"></div>

...

<div th:replace="~{commons/commons::sidebar}"></div>
```











## 一.准备工作



**1.**

**把静态资源导入到IDEA中**

![image-20220822164724752](C:\Users\Nicolasbruno\AppData\Roaming\Typora\typora-user-images\image-20220822164724752.png)

将拿到的静态资源文件中的html文件导入templates中，css/img/js文件则导入static中



**2.**

**先建立两个pojo实体类对象，并通过lombok插件进行方法的实现**

![image-20220822163921655](C:\Users\Nicolasbruno\AppData\Roaming\Typora\typora-user-images\image-20220822163921655.png)

```java
//Employee.java

//员工表
@Data    //lombok
@NoArgsConstructor   //无参构造方法
public class Employee {

    private Integer id;
    private String lastName;
    private String email;
    private Integer gender;//0代表女，1代表男
    private Department department;
    private Date birth;

    //在这里内嵌一个日期
    public Employee(Integer id, String lastName, String email, Integer gender, Department department) {
        this.id = id;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.department = department;
        //默认的创建日期
        this.birth = new Date();
    }
}
```



```java
//Department.java

//部门表
@Data    //lombok
@AllArgsConstructor  //有参构造
@NoArgsConstructor   //无参构造
public class Department {

    private Integer id;
    private String DepartmentName;
}
```



**3.**

**在dao层建立两个与pojo类对应的Dao文件，并模拟数据库中的数据以及增删查改的操作**

![image-20220822164156470](C:\Users\Nicolasbruno\AppData\Roaming\Typora\typora-user-images\image-20220822164156470.png)

```java
//DepartmentDemo.java

//部门dao
@Repository
public class DepartmentDao {

    //模拟数据库中的数据
    private static Map<Integer, Department> departments = null;
    static {
        departments = new HashMap<Integer, Department>();  //创建一个部门表

        departments.put(101,new Department(101,"教学部"));
        departments.put(102,new Department(102,"市场部"));
        departments.put(103,new Department(103,"教研部"));
        departments.put(104,new Department(104,"运营部"));
        departments.put(105,new Department(105,"后勤部"));
    }

    //获得所有部门信息
    public Collection<Department> getDepartments(){
        return departments.values();
    }

    //通过id得到部门
    public Department getDepartment(Integer id){
        return departments.get(id);
    }

}
```

```java
//EmployeeDao.java

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

```







## 二.首页实现

**1.由于在templates下的文件无法直接访问，所以我们要写东西去控制它**

传统的SpringMVC写法是在controller层写一个IndexController来控制跳转到index.html页面

![image-20220822170009215](C:\Users\Nicolasbruno\AppData\Roaming\Typora\typora-user-images\image-20220822170009215.png)



但是在springboot接管之后，可以不用写控制器了，IndexController都可以删掉了，直接在config里面写（**扩展SpringMVC**），通过自己定义的方式去**扩展SpringMVC**：

在config文件夹下写一个Config：例如MyMvcConfig，然后重写addViewControllers（添加视图控制）

```java
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
    }
}
```





**2.由于现在静态资源如css等无法加载进来，但是我们使用了thymeleaf，所以要把html等文件改成thymeleaf的形式**

 改造步骤：（以index.html为例）

```html
index.html:
a:引入xmlns:th="http://www.thymeleaf.org"
<html lang="en" xmlns:th="http://www.thymeleaf.org">
    
b:超链接的改造
<!-- Bootstrap core CSS -->
<link href="asserts/css/bootstrap.min.css" rel="stylesheet">
<!-- Custom styles for this template -->
<link href="asserts/css/signin.css" rel="stylesheet">
	改成
<!-- Bootstrap core CSS -->
<link th:href="@{/css/bootstrap.min.css}" rel="stylesheet">
<!-- Custom styles for this template -->
<link th:href="@{/css/signin.css}" rel="stylesheet">
  
c:图片链接的改造
<img class="mb-4" src="asserts/img/bootstrap-solid.svg" alt="" width="72" height="72">
    改成
<img class="mb-4" th:src="@{/img/bootstrap-solid.svg}" alt="" width="72" height="72">

    
其他的html文件改造步骤同index.html一致
    
但是在线链接比如href="http://getbootstrap.com/docs/4.0/examples/dashboard/#"就不用改造，因为不是本地的

```

但是改造以后可能还是不奏效，可能是因为thymeleaf的缓存问题

所以在application.properties文件里加入一个关闭thymeleaf缓存

```properties
# 关闭thymeleaf模板引擎的缓存
spring.thymeleaf.cache=false
```

之后清除浏览器缓存，然后首页就会加载出css样式了

还可以配置首页根路径：(application.properties)

```properties
# 配置一个根路径，现在访问首页就要输入http://localhost:8080/kuang
server.servlet.context-path=/kuang
```



注意：**所有页面的静态资源都需要使用thymeleaf接管，用 `@{}`**











## 三.页面国际化

1.在resources中建立一个i18n文件夹，里面创建login.properties,login_en_US.properties,login_zh_CN.properties三个文件，

![image-20220822191310722](C:\Users\Nicolasbruno\AppData\Roaming\Typora\typora-user-images\image-20220822191310722.png)

2.这三个文件内容如图所示，然后去application.properties里配置文件所放的真实位置：

```properties
# 我们的配置文件放在的真实位置
spring.messages.basename=i18n.login
```



3.然后改造index.html:

```html
1.<h1 class="h3 mb-3 font-weight-normal">Please sign in</h1>
2.<label class="sr-only">Username</label>
<input type="text" class="form-control" placeholder="Username" required="" autofocus="">

3.<label class="sr-only">Password</label>
<input type="password" class="form-control" placeholder="Password" required="">

4.<label>
<input type="checkbox" value="remember-me"> Remember me
5.</label>
<button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>

	改成

1.<h1 class="h3 mb-3 font-weight-normal" th:text="#{login.tip}">Please sign in</h1>
2.<label class="sr-only" th:text="#{login.username}">Username</label>
<input type="text" class="form-control" th:placeholder="#{login.username}" required="" autofocus="">

3.<label class="sr-only" th:text="#{login.password}">Password</label>
<input type="password" class="form-control" th:placeholder="#{login.password}" required="">


4.		<label>
          <input type="checkbox" value="remember-me"> [[#{login.remember}]]
        </label>
5.<button class="btn btn-lg btn-primary btn-block" type="submit">[[#{login.btn}]]</button>


```

这样改完以后，首页就全部由英文变成中文了。

但是无法实现点击中文就转换成中文版本，点击English就转换为英文版本



4.接着改造index.html文件：

```html
<a class="btn btn-sm">中文</a>
<a class="btn btn-sm">English</a>
	改成
<a class="btn btn-sm" th:href="@{/index.html(l='zh-CN')}">中文</a>
<a class="btn btn-sm" th:href="@{/index.html(l='en-US')}">English</a>
```



5.然后通过看源码，发现一个LocaleResolver这样一个地区解析器，我们就可以在config里写一个解析器MyLocaleResolver来实现这个LocaleResolver接口，然后重写方法,按照源码AcceptHeaderLocaleResolver.class里的LocaleResolver方法照猫画虎重写

```java
public class MyLocaleResolver implements LocaleResolver {
    //解析请求
    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        //获取请求中的语言参数
        String language = httpServletRequest.getParameter("l");
        Locale locale = Locale.getDefault(); //如果没有就使用默认的
        //如果请求的链接携带了国际化参数
        if(!StringUtils.isEmpty(language)){
            //zh_CN
            String[] split = language.split("_");
            //国家，地区
            locale = new Locale(split[0], split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
```

然后这个自己的地区解析器写完之后，要放到Bean里面（就是往容器中注入这样一个组件），然后就去MyMvcConfig里写一个@Bean

```java
//自定义的国际化组件就生效了！
@Bean
public LocaleResolver localeResolver(){
    return new MyLocaleResolver();
}
```

- 注意：
  - 要完成页面国际化：
    - 我们需要配置i18n文件
    - 我们如果需要在项目中进行按钮自动切换，我们需要自定义一个组件`LocaleResolver`
    - 记得将自己写的组件配置到spring容器中 `@Bean`
    - #{}

**ResourceBund界面：安装一个Resource Bundle Editor的插件就有了**



```java
这里在切换中英文的时候会产生一个500错误，但是根据评论区的
/*
P22国际化中文和英文无法切换的问题，首先在html中
<a class="btn btn-sm" th:href="@{/index.html(l='zh_CN')}">中文</a>    
<a class="btn btn-sm" th:href="@{/index.html(l='en_US')}">English</a>
狂神在视频里面的那个不是l,是1 123的1，在视频里一直说l（L）
对应的就是MyLocaleResolver中的这一句
 String language = httpServletRequest.getParameter("l");
类名怕打错就复制就好了，然后就是在自定义Bean中检查好
	@Bean
    public LocaleResolver localeResolver(){
        return new MyLocaleResolver();
    }
*/
这里面的所有跟项目里面一样的代码粘贴过去覆盖自己写的，就能够运行了
        我也不知道为什么，花了我一个多小时抱着试试看的态度粘贴到自己项目里覆盖自己的对应的代码，居然成功了、
```





## 四.登录功能实现

1.先改造index.html

```html
<form class="form-signin" action="dashboard.html">
    	改成
<form class="form-signin" th:action="@{/user/login}">
```

2.然后创建一个LoginController(这里选择让用户直接登录成功，只要名字不空就可以)

```java
@Controller
public class LoginController {

    @RequestMapping("/user/login")
    @ResponseBody
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model){
        //具体的业务：
        if(!StringUtils.isEmpty(username) && "123456".equals(password)){
            return "dashboard";
        }else {
            //告诉用户你登录失败了，用Model传一个信息
            model.addAttribute("msg","用户名或者密码错误");
            return "index";
        }
    }
}
```

3.然后测试一下能正常运行之后，就进行登录失败的信息提示

```html
在index.html的h1下面加上一行这个：

<!--			如果msg的值为空，则不显示消息-->
<p style="color: red" th:text="${msg}" th:if="${not #strings.isEmpty(msg)}"></p>
那么就能够在只有登录失败的时候才显示msg了
```

4.然后在MyMvcConfig里增加一行，以防止信息泄露在url中

```java
registry.addViewController("/main.html").setViewName("dashboard");
//就在登录成功以后url上显示的不是用户名和密码，而是指定的main.html，其实这个main.html是假的html，只是一个名字
```



5.最后在LoginController中重定向到真正的main.html中

```java
把            return "dashboard";
改成			 return "redirect:/main.html";
```





但是现在问题是不论登录不登录，只要知道http://localhost:8080/kuang/main.html这个url，就能够进入到后台页面，所以需要拦截器



## 五.登录拦截器

1.新建一个LoginHandlerInterceptor来实现`HandlerInterceptor`接口以及重写`preHandle`方法

```java
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //登录成功之后，应该有用户的session
        Object loginUser = request.getSession().getAttribute("loginUser");

        if(loginUser==null){//没有登录
            request.setAttribute("msg","没有权限，请先登录"); //还没登录，那就提醒他登录
            request.getRequestDispatcher("/index.html").forward(request,response);
            return  false;
        }else {
            return true;
        }

    }
}
```

拦截器写好以后，要将这个拦截器注册到Bean里面（在`MyMvcConfig`中注册--重写`addInterceptors`方法）

```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LoginHandlerInterceptor())
        .addPathPatterns("/**")
        .excludePathPatterns("/index.html","/","/user/login","/css/**","/js/**","/img/**");
}
```



2.在LoginController的login方法参数签名中加入`HttpSession session` ，并完善具体的session相关业务

```java
@Controller
public class LoginController {

    @RequestMapping("/user/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model, HttpSession session){
        //具体的业务：
        if(!StringUtils.isEmpty(username) && "123456".equals(password)){
            session.setAttribute("loginUser",username);   //在这部分里面加的就是这句代码,其他的和上面的一样
            return "redirect:/main.html";
        }else {
            //告诉用户你登录失败了，用Model传一个信息
            model.addAttribute("msg","用户名或者密码错误");
            return "index";
        }
    }
```



3.把用户登录的名字显示在登录成功的main.html页面上

```html
<a class="navbar-brand col-sm-3 col-md-2 mr-0" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">company name</a>

 	改成

<a class="navbar-brand col-sm-3 col-md-2 mr-0" href="http://getbootstrap.com/docs/4.0/examples/dashboard/#">[[${session.loginUser}]]</a>
```







## 六.展示员工列表

1.新建EmployeeController，并完善相关功能

```java
@Controller
public class EmployeeController {
    @Autowired
    EmployeeDao employeeDao;

    @RequestMapping("/emps")
    public String list(Model model){
        Collection<Employee> employees = employeeDao.getAll();
        model.addAttribute("emps",employees);
        return "emp/list";
    }
}
```

2.修改dashboard.html员工列表相关

3.修改list.html相关，把list.html放入新建的emp文件夹下 (跟th有关的就都是修改了的)

4.新建一个commons/commons.html，把头部导航栏和侧边导航栏抽取出来放到其中

5.修改导航高亮显示，点到哪个导航，哪个就显示高亮（传递参数active并接收判断）

6.修改展示：list.html里的thead和tbody



**tips:**

​	**A.提取公共页面**

​		a.   `th:fragment="sidebar"`

​		b.   `th:replace="~{commons/commons::topbar}"`

​		c.   `如果要传递参数，可以直接使用()传参，然后接收判断即可`

```html
传参具体代码：
<!--传递参数给组件-->
<div th:replace="~{commons/commons::sidebar(active='main.html')}"></div>

接收判断具体代码：
<a th:class="${active=='main.html'?'nav-link active':'nav-link'}" th:href="@{/index.html}">
```

这里用到的是thymeleaf里的   条件运算（三元运算符） ===>  If-then-else: (if) ? (then) : (else)



​	**B.列表循环展示（thead和tbody）**

```html
<thead>
	<tr>
		<th>id</th>
		<th>lastName</th>
        <th>email</th>
        <th>gender</th>
        <th>department</th>
        <th>birth</th>
        <th>操作</th>
    </tr>
</thead>
<tbody>
	<tr th:each="emp:${emps}">
		<td th:text="${emp.getId()}"></td>
        <td>[[ ${emp.getLastName()} ]]</td>   <!--行内写法-->
        <td th:text="${emp.getEmail()}"></td>
        <td th:text="${emp.getGender()==0?'女':'男'}"></td>  
        <!--判断，如果0就女，否则男-->
        <td th:text="${emp.getDepartment()}"></td>
        <!--<td th:text="${emp.getBirth()}"></td>-->
        <td th:text="${#dates.format(emp.getBirth(),'yyyy-MM-dd HH:mm:ss')}">	</td>
        <td>
            <button class="btn btn-sm btn-primary">编辑</button>  
            <!--class是样式，btn是按钮，btn-sm是小按钮，btn-primary是按钮的颜色-->
            <button class="btn btn-sm btn-danger">删除</button>
        </td>
    </tr>
</tbody>
```



## 七.增加员工实现

1.按钮提交

2.跳转到添加页面

3.添加员工成功

4.返回首页

```html
<form action="">
    <div class="form-group">
        <label>LastName</label>
        <input type="text" class="form-control" placeholder="kuangshen">
    </div>
    <div class="form-group">
        <label>Email</label>
        <input type="email" class="form-control" placeholder="123456789@qq.com">
    </div>
    <div class="form-group">
        <label>Gender</label><br>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="gender" value="1">
            <label class="form-check-label">男</label>
        </div>
<div class="form-check form-check-inline">
    <input class="form-check-input" type="radio" name="gender" value="0">
    <label class="form-check-label">女</label>
        </div>
    </div>
    <div class="form-group">
        <label>department</label>
        <select class="form-control">
            <option>1</option>
            <option>2</option>
            <option>3</option>
            <option>4</option>
            <option>5</option>
        </select>
    </div>
    <div class="form-group">
        <label>Birth</label>
        <input type="text" class="form-control" placeholder="kuangstudy">
    </div>
    <button type="submit" class="btn btn-primary">添加</button>
</form>    
```



1.增加一个add.html（直接复制list.html，然后用上面那段表单模板代码覆盖掉list.html里的表单代码），

然后再加上name属性，在下拉列表框加上所有部门信息的读取

`<option th:each="dept:${departments}" th:text="${dept.getDepartmentName()}" th:value="${dept.getId()}"></option>`

信息肯定是要从后端传过来，所以在EmployeeController里写一个toAddpage方法

```java
@GetMapping("/emp")
public String toAddpage(Model model){
    //查出所有部门的信息
    Collection<Department> departments = departmentDao.getDepartments();
    model.addAttribute("departments",departments);
    return "emp/add";
}
```

然后点击添加按钮的一瞬间会走到form表单里的一个action请求，<form th:action="@{/emp}" method="post">

然后根据这个action请求再在EmployeeController里写一个addEmp方法

```java
@PostMapping("/emp")
public String addEmp(Employee employee){
    System.out.println("save===>"+employee);
    employeeDao.save(employee);//调用底层业务方法保存员工的信息
    return "redirect:/emps";
}
```



springboot默认的日期格式是`dd/MM/yyyy`

但是可以在application.properties里手动设置一个格式

```properties
spring.mvc.date-format=yyyy-MM-dd
```

如果在添加数据的时候日期格式不正确，就会报400错误



## 八.修改员工信息

1.在list.html中的编辑处写一个 th:href="@{/emp/}+${emp.getId()}"

```html
<a class="btn btn-sm btn-primary" th:href="@{/emp/}+${emp.getId()}">编辑</a>
```

2.在EmployeeController写一个：去员工的修改页面（restful风格）的方法toUpdateEmp

```java
//model是给前端返回数据用的
@GetMapping("/emp/{id}")
public String toUpdateEmp(@PathVariable("id")Integer id,Model model){
    //查出原来的数据
    Employee employee = employeeDao.getEmployeeById(id);
    model.addAttribute("emp",employee);

    return "emp/update";
}
```

3.增加一个update.html（复制一个add.html然后修改）

4.在EmployeeController写一个：员工信息修改的方法：updateEmp

```java
//员工信息修改
@PostMapping("updateEmp")
public String updateEmp(Employee employee){
    employeeDao.save(employee);
    return "redirect:/emps";
}
```

但是做完之后，发现没把id放在隐藏域里，就导致每次编辑完，提交以后都会新增一条而不是改变原有的数据

所以我们在update.html里把id放在隐藏域里携带过去。

```html
<!--把id放在隐藏域里携带过去-->
<input type="hidden" name="id" th:value="${emp.getId()}">
```



注意：**日期格式化**很重要，springboot会严格按照配置文件里的spring.mvc.date-format=yyyy-MM-dd去执行，如果不是这个格式，提交就会报400错误





## 九.删除以及404处理

### **删除**：修改list页面的删除处

```html
<a class="btn btn-sm btn-danger" th:href="@{/delemp/}+${emp.getId()}">删除</a>
```

在EmployeeController写一个删除方法

```java
//删除员工(restful风格）
@GetMapping("/delemp/{id}")
public String deleteEmp(@PathVariable("id")  Integer id){
    employeeDao.delete(id);
    return "redirect:/emps";
}
```



### 404处理：

在springboot中404错误处理非常简单，在templates文件夹下建一个404.html就好，报了错就自动会去找这个文件夹

500错误同理



### 注销

在commons/commons.html里面修改注销处的语句

```html
<a class="nav-link" th:href="@{/user/logout}">注销</a>
```

在LoginController里面写注销的功能

```java
//注销
@RequestMapping("/user/logout")
public String logout(HttpSession session){
    session.invalidate();
    return "redirect:/index.html";
}
```

