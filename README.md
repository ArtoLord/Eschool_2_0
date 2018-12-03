## Документация по классу Route()
### Login
``` Java
Route.login(String username, String password, Callback<State> callback)
```
Класс State - Java-класс, в виде которого возвращается информация о пользователе:
```Java
public class State {
    int userId; 
    String username;
    String prsFio; // фамилия, имя и отчество пользователя в форме Ф И О
}

```
### getPeriods
``` Java
Route.getPeriods(int yearNumber, Callback<ArrayList<Period>> callback)
```
Класс Period содержит информацию о периоде:
``` Java
public class Period {
    public int periodId;
    public String periodName;
    public boolean isStudy;
}
```
### getMarks
``` Java
Route.getMarks(int userid, int periodId, Callback<ArrayList<Unit>> callback)
```
Класс Unit содержит информацию об оценках по данному предмету:
``` Java
public class Unit {
    public String unitName; //Название предмета
    public Double overMark; // Средний балл
    public String rating; // рейтинг
    public String totalmark; // итоговая оценка
}
```
### getMarksWithWights
``` Java
Route.getMarksWithWights(int userid, int periodId, Callback<ArrayList<Mark>> callback)
```
Класс Mark содержит информацию о конкретной оценке:
``` Java
public class Mark {
    public Integer markVal;
    public String subject;
    public Double mktWt;
    public Integer unitId;
}
```
### save(Context) и load(Context)
Эти функции получаю из `SharedPreferences` информацию о cookie и сохраняют ее в классе `Route` и наобарот. 
>Вызывайте их после того, как инициализируете класс `Route` или получите информацию о пользователе.
### Callback < T >
Интерфейс, который должен быть реализован вашим классом, который вы передаете в функцию класса Route.
Этот класс должен реализовать функцию `callback(T)`, которая вызывается при успешном запросе, и функцию `onError(int errorId)`,
которая вызывается в случае ошибки в запросе и выдает разные errorId, в зависимости от ошибки:
```Java
Constants.LoginError; //ошибка при авторизации
Constants.StateError; //ошибка при получении информации о пользователе
Constants.PeriodError; //ошибка при получении информации о периодах
Constants.UnitError; //ошибка при получении информации об оценках
Constants.CookieError; //cookie = null, возможно вы не вызвали Route().login() и в SharedPreferensice не сохранена информация о cookie
```

    
> все запросы выполняются *асинхронно*, поэтому и надо использовать callback
> Route(Context) нужно вызывать только после запуска Activity
