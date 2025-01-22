package issue;

import java.util.List;

import com.caoccao.javet.annotations.V8Function;

public class Fns {
    @V8Function
    public static int[] getArray() {
        return new int[] {3, 4, 5};
    }

    @V8Function
    public static List<Integer> getList() {
        return List.of(3, 4, 5);
    }
}
