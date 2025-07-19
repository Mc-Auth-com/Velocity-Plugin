> [!NOTE]  
> Please check [Mc-Auth-com/Mc-Auth](https://github.com/Mc-Auth-com/Mc-Auth) for more details about the Mc-Auth project.

---

# Mc-Auth Velocity Plugin [![Gradle][GitHub Actions Badge]][GitHub Actions Link] [![Quality Gate Status][SonarQube Badge]][SonarQube Link]

This is a [Velocity](https://papermc.io/software/velocity) plugin used by Mc-Auth to very Minecraft users
connecting to it, generating a 6-digit code
and presenting it to the user for them to use on the Mc-Auth website.

## Development
You can run `./gradlew runVelocity` to build the project
and spin up a local Velocity server with that plugin built.

### Building
* You can build the project using `./gradlew shadowJar`
* The built jar will be located in `plugin/build/libs/`


[GitHub Actions Badge]: https://github.com/Mc-Auth-com/Velocity-Plugin/actions/workflows/gradle.yml/badge.svg
[GitHub Actions Link]: https://github.com/Mc-Auth-com/Velocity-Plugin/actions/workflows/gradle.yml
[SonarQube Badge]: https://sonarcloud.io/api/project_badges/measure?project=Mc-Auth-com_Velocity-Plugin&metric=alert_status
[SonarQube Link]: https://sonarcloud.io/summary/new_code?id=Mc-Auth-com_Velocity-Plugin
