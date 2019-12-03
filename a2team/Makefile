.PHONY = compile run jar runjar zip all clean

#TODO: edit with path to your javac (java compiler)
JC = /usr/lib/jvm/java-11-openjdk-amd64/bin/javac

#TODO: edit with path to your java (java runtime environment)
JRE = /usr/lib/jvm/java-11-openjdk-amd64/bin/java

#TODO: edit with path to your module-path for javafx
MP = --module-path /home/yichen/javafx-sdk-11.0.2/lib --add-modules javafx.controls,javafx.fxml -Dfile.encoding=UTF-8

#TODO: edit with your classpath from Eclipse 
CP = -classpath -classpath /home/yichen/eclipse-workspace/a2team:/home/yichen/javafx-sdk-11.0.2/lib/javafx.base.jar:/home/yichen/javafx-sdk-11.0.2/lib/javafx.controls.jar:/home/yichen/javafx-sdk-11.0.2/lib/javafx.fxml.jar:/home/yichen/javafx-sdk-11.0.2/lib/javafx.graphics.jar:/home/yichen/javafx-sdk-11.0.2/lib/javafx.media.jar:/home/yichen/javafx-sdk-11.0.2/lib/javafx.swing.jar:/home/yichen/javafx-sdk-11.0.2/lib/javafx.web.jar:/home/yichen/javafx-sdk-11.0.2/lib/javafx-swt.jar:/home/yichen/javafx-sdk-11.0.2/lib/src.zip application.Main


SRC = application/*.java   

APP = application.Main 

compile:
	$(JC) $(CP) $(SRC) 

run:
	$(JRE) $(MP) $(CP) $(APP) $(ARGS)

jar:
	jar -cvmf manifest.txt executable.jar .

runjar:
	$(JRE) $(MP) -jar executable.jar $(ARGS)

# Create zip file for submitting to handin
zip: 
	zip -r ateam.zip .

#Eclipse's "Show Command Line"
all:
	/usr/lib/jvm/java-11-openjdk-amd64/bin/java --module-path /home/yichen/javafx-sdk-11.0.2/lib --add-modules javafx.controls,javafx.fxml -Dfile.encoding=UTF-8 -classpath /home/yichen/eclipse-workspace/a2team:/home/yichen/javafx-sdk-11.0.2/lib/javafx.base.jar:/home/yichen/javafx-sdk-11.0.2/lib/javafx.controls.jar:/home/yichen/javafx-sdk-11.0.2/lib/javafx.fxml.jar:/home/yichen/javafx-sdk-11.0.2/lib/javafx.graphics.jar:/home/yichen/javafx-sdk-11.0.2/lib/javafx.media.jar:/home/yichen/javafx-sdk-11.0.2/lib/javafx.swing.jar:/home/yichen/javafx-sdk-11.0.2/lib/javafx.web.jar:/home/yichen/javafx-sdk-11.0.2/lib/javafx-swt.jar:/home/yichen/javafx-sdk-11.0.2/lib/src.zip application.Main

# Remove generated files
clean:
	rm -f application/*.class
	rm -f executable.jar

