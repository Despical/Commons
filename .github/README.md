<h1 align="center">Commons</h1>

<div align="center">

[![](https://github.com/Despical/Commons/actions/workflows/build.yml/badge.svg)](https://github.com/Despical/Commons/actions/workflows/build.yml)
[![](https://img.shields.io/maven-central/v/com.github.despical/commons.svg?label=Maven%20Central)](https://repo1.maven.org/maven2/com/github/despical/commons)
[![](https://img.shields.io/badge/License-GPLv3-blue.svg)](../LICENSE)
[![](https://img.shields.io/badge/Javadoc-latest-blue.svg)](https://despical.github.io/Commons)

Commons is an open-source library that provides useful utilities for Java and Minecraft.

</div>

## Documentation
- [Wiki](https://github.com/Despical/Commons/wiki)
- [Javadocs](https://despical.github.io/Commons)
- [Maven Central](https://repo1.maven.org/maven2/com/github/despical/commons)
- [Sonatype Central](https://central.sonatype.com/artifact/com.github.despical/commons)

## Using the Commons

### Maven
```xml
<dependency>
    <groupId>com.github.despical</groupId>
    <artifactId>commons</artifactId>
    <version>1.9.9</version>
</dependency>
```

### Gradle
```gradle
dependencies {
    implementation 'com.github.despical:commons:1.9.9'
}
```

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
