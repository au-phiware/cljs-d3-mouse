.PHONY: clean open test

target: cljs.jar build.clj $(shell find src resources node_modules 2>/dev/null)
	rsync -r resources/ target/
	java -cp cljs.jar:src clojure.main build.clj

node_modules:
	mkdir node_modules

open: target
	open target/index.html

test: target
	chrome --headless --dump-dom file://$(CURDIR)/target/index.html

cljs.jar:
	ln -s $(CLJS_HOME)/target/cljs.jar

clean:
	rm -rf target node_modules
