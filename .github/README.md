<h1 align="center">Commons</h1>

<div align="center">

[![](https://github.com/Despical/Commons/actions/workflows/build.yml/badge.svg)](https://github.com/Despical/Commons/actions/workflows/build.yml)
[![](https://img.shields.io/github/v/release/Despical/Commons)](https://github.com/Despical/Commons/releases/latest)
[![](https://jitpack.io/v/Despical/Commons.svg)](https://jitpack.io/#Despical/Commons)
[![](https://img.shields.io/badge/License-GPLv3-blue.svg)](../LICENSE)
[![](https://img.shields.io/badge/javadoc-latest-lime.svg)](https://javadoc.jitpack.io/com/github/Despical/Commons/latest/javadoc/index.html)

Commons is an open-source library that provides useful utilities for Java and Minecraft.

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
    <version>1.9.2</version>
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
    implementation 'com.github.Despical:Commons:1.9.2'
}
```

</details>

## License
This code is under [GPL-3.0 License](http://www.gnu.org/licenses/gpl-3.0.html).

See the [LICENSE](https://github.com/Despical/Commons/blob/master/LICENSE) file for required notices and attributions.

## Donations
- [Patreon](https://www.patreon.com/despical)
- [Buy me a Coffee](https://www.buymeacoffee.com/despical)

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

> [!IMPORTANT]  
> Don't forget to install Maven before building.
