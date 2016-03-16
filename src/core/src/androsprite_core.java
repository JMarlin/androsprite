
public class androsprite_core {
    
    //Thought here is that all of the core bitmap stuff will be done in a
    //non platform-specific way internally by representing them all as
    //array objects, blitting between each other using:
    // http://docs.oracle.com/javase/6/docs/api/java/lang/System.html#arraycopy%28java.lang.Object,%20int,%20java.lang.Object,%20int,%20int%29
    //and then having specific desktop and android methods for rendering the
    //final output onto the screen
}
