## libevent-2.0.21
BASE=${shell pwd}
libevent=./libevent-2.0.21
LIBSDIR=$(BASE)/../../libs

.PHONY: clean  all

all: 
	cd $(libevent);              \
	./configure --prefix=$(BASE) CC=$(CROSS_COMPILE)gcc --build=i686-pc-linux --host=$(HOST) --disable-openssl;  \
	$(MAKE) clean; $(MAKE); $(MAKE) install
	-@rm  -f lib/libevent.a
	-@rm  -f lib/libevent.so
	-@cp     lib/libevent-2.0.so.5   lib/libevent.so   
	-@cp     lib/libevent-2.0.so.5   $(LIBSDIR)/
	-@cd $(LIBSDIR) && rm -f libevent.so && ln -sf libevent-2.0.so.5  libevent.so
 
clean:
	-@rm  -rf   lib
	-@rm  -rf   bin
	-@rm  -rf   include
	-@rm  -r   $(LIBSDIR)/libevent.so
	-@rm  -r   $(LIBSDIR)/libevent-2.0.so.5
