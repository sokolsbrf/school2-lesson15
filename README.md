# ШКОЛА 2, ЗАНЯТИЕ 15
# Практикум фрагментов

Приложение показывает установленные пакеты.

Оно не перетендует на архитектурную чистоту, в частности:

1. Каждый раз создаёт ```AsyncTask``` вместо использования контролируемого пула потоков
2. Страдает синглтонитом
3. В целом написано достаточно небрежно, и выполняет много лишних действий (оповещает всех слушателей
   обо всех пакетах, хотя их обычно интересует лишь единственный)


Но при этом оно всё же демонстрирует:

1. Правильную работу с контекстом (долго хранить можно лишь application context), а подписанные 
   короткоживущие объекты UI (фрагменты) отписываются немедленно при уничтожении, и сбрасывают
   подписки всех своих внутренних объектов (адаптера)
2. Правильный вызов фрагментами друг друга (через контракт, обращаясь к ```Activity```)
3. Минимизацию работы на UI thread
4. Работу ```PackageManager``` в том числе: немедленно доступные данные, и загружаемые долго
5. Разумеется, всё вынесено в ресурсы, и создана иконка
6. Промежуточные POJO по возможности immutable

**Здесь могут попадаться ошибки** и отступления от сказанного. Сложно всё сделать идеально, когда
рассказываешь аудитории что пишешь, и плюс на дворе суббота. Внимательно следите, задавайте вопросы.