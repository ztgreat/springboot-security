# springboot-security

基于spring boot,spring-security,mybatis(使用mybatis-plus)的简易的一个后台权限模块,只包含权限部分

前端展示配合 [react-authority](https://github.com/ztgreat/react-authority)

此外 另一个权限[springboot-shiro](https://github.com/ztgreat/springboot-shiro) 项目使用shiro 重写了权限部分，从功能上来说是一致的

## spring security

基于资源的访问控制（实质也是角色访问控制），根据请求资源，判断用户是否拥有该资源，进而判断是否允许用户访问，前端使用ant-design-pro来简单的实现，并没有很完善化，可以具体到某个action,展示界面未到按钮级别进行控制。
url 资源判断复用了shiro 中的模块（WildcardPermission）,具体代码中有注释


### 菜单管理

![菜单管理](./pics/菜单管理.png)



### 资源权限

![资源权限](./pics/资源权限.png)



### 角色管理

![角色管理](./pics/角色管理.png)

可以给每个角色分配菜单和资源

### 角色分配

![角色分配](./pics/角色分配.png)



![角色分配2](./pics/角色分配2.png)

## session

session 统一放到redis 中进行管理

