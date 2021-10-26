# Commons
[![](https://jitpack.io/v/Despical/Commons.svg)](https://jitpack.io/#Despical/Commons)
[![](https://img.shields.io/badge/Javadocs-latest-lime.svg)](https://javadoc.jitpack.io/com/github/Despical/Commons/latest/javadoc/index.html)
[![](https://img.shields.io/discord/719922452259668000.svg?color=lime&label=Discord)](https://discord.gg/Vhyy4HA)
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/Despical/Commons/Commons%20Build)

Commons is a open-source library that provides utilities needed for Java and Minecraft.

## Documentation
More information will be found on the [wiki page](https://github.com/Despical/Commons/wiki) soon. The [Javadoc](https://javadoc.jitpack.io/com/github/Despical/Commons/latest/javadoc/index.html) can be browsed. Questions
related to the usage of Commons should be posted on my [Discord server](https://discord.com/invite/Vhyy4HA).

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
    <artifactId>Commons</artifactId>
    <version>1.4.0</version>
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
    compileOnly group: "com.github.Despical", name: "Commons", version: "1.4.0";
}
```

## License
This code is under [GPL-3.0 License](http://www.gnu.org/licenses/gpl-3.0.html)

See the [LICENSE](https://github.com/Despical/Commons/blob/master/LICENSE) file for required notices and attributions.

## Donations
You like the Commons? Then [donate](https://www.patreon.com/despical) back me to support the development.

## ~~Contributing~~ (No more contributing)

I accept Pull Requests via GitHub. There are some guidelines which will make applying PRs easier for me:
+ No spaces! Please use tabs for indentation.
+ Respect the code style.
+ Create minimal diffs. If you feel the source code should be reformatted create a separate PR for this change.

You can learn more about contributing via GitHub in [contribution guidelines](https://github.com/Despical/Commons/blob/master/CONTRIBUTING.md).

## Building from source
If you want to build this project from source code, run the following from Git Bash:
```
git clone https://www.github.com/Despical/Commons.git && cd Commons
mvn clean package
```
The build can then be found in ``/Commons/target/``
And also don't forget to install Maven before building.
