package businessLogConfig

/**
 * User: zjzhai
 * Date: 3/6/14
 * Time: 2:32 PM
 */
class ProjectConfig {

    def context

    def ProjectApplicationImpl_findSomeProjects(){
        "ProjectApplicationImpl_findSomeProjects ${context.xx}"
    }

}
