# Commons
[![](https://jitpack.io/v/Despical/Commons-Box.svg)](https://jitpack.io/#Despical/Commons-Box)
[![](https://img.shields.io/badge/javadocs-latest-lime.svg)](https://javadoc.io/doc/com.github.Despical/commons/latest/index.html)
[![discord](https://img.shields.io/discord/719922452259668000.svg?color=lime&label=discord)](https://discord.gg/Vhyy4HA)

Commons is a open-source library that provides utilities needed for Java and Minecraft.

## Documentation
More information will be found on the [wiki page](https://github.com/Despical/Commons/wiki) soon. The [Javadoc](https://javadoc.io/doc/com.github.Despical/commons/latest/index.html) can be browsed. Questions
related to the usage of Command Framework should be posted on my [Discord server](https://discord.com/invite/Vhyy4HA).

## Using Commons
The project isn't in the Central Repository yet, so specifying a repository is needed.<br>
To add this project as a dependency to your project, add the following to your pom.xml:

### Maven dependency

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
```xml
<dependency>
    <groupId>com.github.Despical</groupId>
    <artifactId>commons</artifactId>
    <version>1.2.3</version>
    <scope>compile</scope>
</dependency>
```

### Gradle dependency
```
repositories {
    maven { url 'https://jitpack.io' }
}
```
```
dependencies {
    compileOnly group: "com.github.Despical", name: "commons", version: "1.2.3";
}
```

## License
This code is under [GPL-3.0 License](http://www.gnu.org/licenses/gpl-3.0.html)

See the [LICENSE.txt](https://github.com/Despical/Commons/blob/master/LICENSE) file for required notices and attributions.

## Donations
You like the Commons? Then [donate](https://www.patreon.com/despical) back me to support the development.

## Contributing

I accept Pull Requests via GitHub. There are some guidelines which will make applying PRs easier for me:
+ No spaces! Please use tabs for indentation.
+ Respect the code style.
+ Create minimal diffs. If you feel the source code should be reformatted create a separate PR for this change.

You can learn more about contributing via GitHub in [contribution guidelines](CONTRIBUTING.md).

## Building from source
If you want to build this project from source code, run the following from Git Bash:
```
git clone https://www.github.com/Despical/Commons.git && cd Commons
mvn clean package
```
The build can then be found in ``/Commons/target/``
And also don't forget to install Maven before building.
