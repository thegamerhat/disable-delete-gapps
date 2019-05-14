SDK=~/Android/Sdk
TARGET=28
TOOL=28.0.3
JAVADIR=$(JAVA_HOME)/bin
BUILDTOOLS=$(SDK)/build-tools/$(TOOL)
AJAR=$(SDK)/platforms/android-$(TARGET)/android.jar
ADX=$(BUILDTOOLS)/dx
AAPT=$(BUILDTOOLS)/aapt
JAVAC=$(JAVADIR)/javac
JARSIGNER=$(JAVADIR)/jarsigner
APKSIGNER=$(BUILDTOOLS)/apksigner
ZIPALIGN=$(BUILDTOOLS)/zipalign
KEYTOOL=$(JAVADIR)/keytool
ADB=$(SDK)/platform-tools/adb

KEYFILE=keystore.jks

SRC=src/
NAME=app


all: clear build zipalign sign
build:
	mkdir bin
	mkdir gen
	$(AAPT) package -v -f -I $(AJAR) -M AndroidManifest.xml -A assets -S res -m -J gen -F bin/resources.ap_
	$(JAVAC) -classpath $(AJAR) -sourcepath $(SRC) -sourcepath gen -d bin $(shell find $(SRC) -name *.java)
	$(ADX) --dex --output=bin/classes.dex bin
	mv bin/resources.ap_ bin/$(NAME).ap_
	cd bin ; $(AAPT) add $(NAME).ap_ classes.dex
zipalign:
	$(ZIPALIGN) -v -p 4 bin/$(NAME).ap_ bin/$(NAME)-aligned.ap_
	mv bin/$(NAME)-aligned.ap_ bin/$(NAME).ap_
optimize:
	optipng -o7 $(shell find res -name *.png)
sign:
	$(APKSIGNER) sign --ks $(KEYFILE) --out bin/$(NAME).apk bin/$(NAME).ap_
	rm -f bin/$(NAME).ap_
clear:
	rm -rf bin gen
install:
	$(ADB) install -r bin/$(NAME).apk
