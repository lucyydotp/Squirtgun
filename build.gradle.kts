subprojects {
    version = "1.0.0-SNAPSHOT"
    group = "me.lucyy"

    apply<JavaPlugin>()
    apply<MavenPublishPlugin>()
    apply<SigningPlugin>()
}