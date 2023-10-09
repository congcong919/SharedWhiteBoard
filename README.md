# SharedWhiteBoard
This project is a shared white board program that allows multiple users to draw simultaneously on a canvas supporting drawing line, oval, rectangle, circle, free hand, texting as well as chatting and change color and host can have some other operations like kicking the joined user and save or create current whiteboard.<br><br>
**Application design**<br>
Client-Sever model for system architecture<br>
Allow multiple users to use the application at the same time based on thread-per-connection mode<br>
Socket network technique for operation sharing between each user<br>
Java GUI supported library for UI<br><br>
**Functions**<br>
Users can draw line, oval, rectangle, circle, free hand and typing and can select drawing or typing color<br>
Users can chat with each other in the chatting area<br>
Host can kick user in the same session<br>
Host can create and close the session<br>
Host can save all drawings on the whiteboard as .txt file which can be used for recover all drawings or next opening. In addition, host can save drawings as .jpg format<br>


# To run the jar file in the terminal(CMD)
-For host <br>
**java -jar host.jar localhost 8006 hostname** <br>
example: java -jar host.jar localhost 8006 Alice <br>

-For guest <br>
**java -jar guest.jar localhost 8006 guestname** <br>
example: java -jar guest.jar localhost 8006 Bob <br>
