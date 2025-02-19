version = version("1.0.1")

dependencies {
    compileOnlyApi(project(":common:addons:manifest-addon-loader"))
    
    implementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
    testImplementation("net.jafama", "jafama", Versions.Libraries.Internal.jafama)
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    relocate("net.jafama", "com.dfsek.terra.addons.biome.pipeline.lib.jafama")
}