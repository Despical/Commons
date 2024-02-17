<h1 align="center">Commons</h1>

<div align="center">

[![Commons Build](https://github.com/Despical/Commons/actions/workflows/build-commons.yml/badge.svg?branch=master)](https://github.com/Despical/Commons/actions/workflows/build-commons.yml)
[![](https://jitpack.io/v/Despical/Commons.svg)](https://jitpack.io/#Despical/Commons)
[![](https://img.shields.io/badge/Javadocs-latest-lime.svg)](https://javadoc.jitpack.io/com/github/Despical/Commons/latest/javadoc/index.html)
[![Support](https://img.shields.io/badge/Patreon-Support-lime.svg?logo=Patreon)](https://www.patreon.com/despical)
[![](https://img.shields.io/badge/BuyMeACoffee-Support-lime.svg?logo=BuyMeACoffee)](https://www.buymeacoffee.com/despical)

Commons is a open-source library that provides utilities needed for Java and Minecraft.

</div>

## Documentation
- [Wiki](https://github.com/Despical/Commons/wiki)
- [JavaDocs](https://javadoc.jitpack.io/com/github/Despical/Commons/latest/javadoc/index.html)

## Using Commons
The project isn't in the Central Repository yet, so specifying a repository is needed.<br>

<details>
<summary>Maven dependency</summary>

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
    <version>1.7.1</version>
    <scope>compile</scope>
</dependency>
```

</details>

<details>
<summary>Gradle dependency</summary>

```
repositories {
    maven { url 'https://jitpack.io' }
}
```
```
dependencies {
    compileOnly group: "com.github.Despical", name: "Commons", version: "1.7.1";
}
```

</details>

## License
This code is under [GPL-3.0 License](http://www.gnu.org/licenses/gpl-3.0.html)

See the [LICENSE](https://github.com/Despical/Commons/blob/master/LICENSE) file for required notices and attributions.

## Donations
- [Patreon](https://www.patreon.com/despical)
- [Buy me a Coffe](https://www.buymeacoffee.com/despical)

## Contributing

I accept Pull Requests via GitHub. There are some guidelines which will make applying PRs easier for me:
+ No spaces! Please use tabs for indentation.
+ Respect the code style.
+ Create minimal diffs. If you feel the source code should be reformatted create a separate PR for this change.

You can learn more about contributing via GitHub in [contribution guidelines](https://github.com/Despical/Commons/blob/master/CONTRIBUTING.md).

## Building from source
To build this project from source code, run the following from Git Bash:
```
git clone https://www.github.com/Despical/Commons.git && cd Commons
mvn clean package -Dmaven.javadoc.skip=true -DskipTests
```

> **Note** Don't forget to install Maven before building.
