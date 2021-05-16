subprojects {
    version = "2.0.0-SNAPSHOT"
    group = "me.lucyy"

    apply<MavenPublishPlugin>()
    apply<SigningPlugin>()
    apply<JavaPlugin>()

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}