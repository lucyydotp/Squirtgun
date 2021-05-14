subprojects {
    version = "2.0.0-SNAPSHOT"
    group = "me.lucyy"

    apply<JavaPlugin>()
    apply<MavenPublishPlugin>()
    apply<SigningPlugin>()
}