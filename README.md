# PolyChat
[JAVA] Socket UDP
Project made during my network curse at Polytech Lyon 1 (french engineering school)

*********************************************************************************************************************************************
HOW TO USE ?

1) You will first be asked to create (or not) a server. If you don't have a server to communicate with, type O (letter o). Otherwise, type N.
2) Enter server's parameters : IP address, port and also a range of ports that you want the server to listen.
3) Same as number one, you will be asked to create or not a client. Type O if you want to, N if you don't
4) Enter IP address and port of the server you want to communicate with
5) You will be able to write messages
6) If you created a client, type the word "stop" if you want to quit
7) Communication will be over

*******************************************************************************************************************************************
FEATURES
- Each client, server and communication has its own thread (multi threading)
- You can type messages on your keyboard
- The server can handle multiple communications at the same time
- Server can handle port's assignement having a reduced range of ports
- You can stop the communication on the client with the word "stop"

*******************************************************************************************************************************************
SOON
- Handle French special caracters (such as : é, è, ç, etc.)
- Check if IP address, port are correctly typed by the user
- Handle port's status when the communication is over (it is supposed to be open again)


