# mapstruct-fluent-accessors-spi

# The latest Release : 1.0.2

To get a release this Git project into your Java project build:

Step 1. Add the JitPack repository to your build file


	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

Step 2. Add the dependency

	<dependency>
	    <groupId>com.github.nilyin</groupId>
	    <artifactId>mapstruct-fluent-accessors-spi</artifactId>
	    <version>1.0.2.</version>
	</dependency>


Step 3. Add new path in AnnotationProcessor

	<annotationProcessorPaths>
		<path>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-processor</artifactId>
            <version>${org.mapstruct.version}</version>
        </path>
        <path>
            <groupId>com.github.nilyin</groupId>
            <artifactId>mapstruct-fluent-accessors-spi</artifactId>
            <version>1.0.2</version>
        </path>
    </annotationProcessorPaths>