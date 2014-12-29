TARGET: build

PROJECTS=application

build:
	for project in $(PROJECTS); do \
		make -C $$project $@ || exit $? ; \
	done
	
	
install:
	for project in $(PROJECTS); do \
		make -C $$project $@ || exit $? ; \
	done

clean:
	for project in $(PROJECTS); do \
		make -C $$project $@ || exit $? ; \
	done

.PHONY: build install clean
