default:
	@make arch
	@make asm
	@rm *.class
	@mv *.jar ./bin/
arch:
	@rm ./manifest/manifest.mf
	@echo "Main-class: arch" >> ./manifest/manifest.mf
	@javac -d . arch.java
	@jar -cmf ./manifest/manifest.mf arch.jar arch.class
asm:
	@rm ./manifest/mf1.mf
	@echo "Main-class: asm" >> ./manifest/mf1.mf
	@javac -d . asm.java
	@jar -cmf ./manifest/mf1.mf asm.jar asm.class