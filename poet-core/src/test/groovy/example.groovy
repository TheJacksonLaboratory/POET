import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class example extends Specification {

    void "fake test stuff"(){
        when:
        def omega = "lul"

        then:
        omega == "lul"
    }
}
