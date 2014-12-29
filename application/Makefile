SBT=./sbt.make
SBT_ARGS=-Dsbt.log.noformat=true

INSTALL_DIR=$(DESTDIR)/usr/share/topsec/base

VERSION_FILE=conf/system.version
VERSION_STRING=application.version=

VERSION=$(shell grep "$(VERSION_STRING)" $(VERSION_FILE) | sed -e "s/$(VERSION_STRING)\"\(.*\)\"/\1/")

version:
	@echo $(VERSION)

check_version:
	@test ! ".$(VERSION)" = "." || exit 1

build:
	$(SBT) $(SBT_ARGS) clean
	$(SBT) $(SBT_ARGS) dist
	
install: check_version
	mkdir -p $(INSTALL_DIR)/java-frontend
	cd target/universal/ && unzip -o ts-base-frontend-$(VERSION).zip && \
		mv ts-base-frontend-$(VERSION)/lib $(INSTALL_DIR)/java-frontend/

clean:
	#clean is called by dpkg-buildpackage with fakeroot, does not work

lib-list:
	@echo -n "# This file was created by 'make lib-list' in base frontend version '$(VERSION)'.\n#\n# Remember to update.\n#\n" > lib_list.txt
	( cd target/universal && unzip -o ts-base-frontend-$(VERSION).zip 1>/dev/null && \
		cd ts-base-frontend-$(VERSION)/lib && for lib in *.jar; \
		do echo $$lib; done ; \
	) >> lib_list.txt

.PHONY: build install clean version
