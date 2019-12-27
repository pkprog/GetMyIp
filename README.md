# GetMyIp
Цель: Получить IP-адрес удалённой машины
Причина: ногда без домена в локалке скачет Ip-адрес компьютера. Это один из вариантов решения.

Этот сервис должен работать на удалённом компьютере. Его можно запусться, например, через программу srvany. Сервис отслеживает поступившие письма, и, если появилось письмо с темой, например, "GET MY IP", то отправляет другим письмом свой ip.

То есть сервис отслеживает наличие письма с просьбой определить и показать IP. Как только письмо появляется, то сервис создаёт новое письмо с его IP-адресом

Реализовано только для протокола POP3. Для IMAP - не реализовано.

<h3>Настройка</h3>
Найстройте application.property. 
Если для почты yandex, то в нём нужно указать mail.username, mail.password, mail.send.fromEmail, mail.send.toEmail. Плюс в настройках яндекса (Почта → Все настройки → Почтовые программы) поставить галку POP3 и выбрать папку, которая будет сканироваться.Но название этой папки для POP3 в application.property не настраивается!
Для не-yandex ещё нужно указать порты и сервера.
Новый application.property нужно положить куда-нибудь в файловой системе. Путь к нему указывается в файле gmiservice.reg в последне  части строки AppParameters

Скрипты для запуска как сервис Windows (тестировано на Win7) скрипты в папке runonwondows. 
Информация для запуска в качестве сервиса в основном взята из источников: 
<ul style="list-style-type: none;">
  <li>
https://habr.com/ru/sandbox/49165/
  </li>
  <li>
https://social.msdn.microsoft.com/Forums/vstudio/en-US/28400ffa-f905-4b6b-97f7-2df1166849f9/error-1083-the-executable-program-that-this-service-is-configured-to-run-in-does-not-implement-the?forum=vcgeneral   (сообщение Wednesday, January 25, 2012 10:52 AM)
  </li>
</ul>
