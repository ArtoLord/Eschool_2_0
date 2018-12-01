## Документация по классу Route
``` Kotlin
Route.login(String username, String password, CallBackInterface callback)
```
`CallBackInterface` должен быть реализован вашим классом, который вы должны передать в Route.login().
В нем должна быть реализована функция  `callback` : 
``` Kotlin
override fun callback(user: State){
//Your code here
}
```
Класс State - Java-класс, в виде которого возвращается информация о пользователе:
```Java
public class State {
    int userId; 
    String username;
    String prsFio; // фамилия, имя и отчество пользователя в форме Ф И О

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPrsFio(String prsFio) {
        this.prsFio = prsFio;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPrsFio() {
        return prsFio;
    }
}

```
> все запросы выполняются *асинхронно*, поэтому и надо использовать callback
