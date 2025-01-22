package issue;

import com.caoccao.javet.interception.logging.JavetStandardConsoleInterceptor;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.options.V8RuntimeOptions;

public class Main {
    public static void main(String[] args) throws Exception {
        var fns = new Fns();
        var v8Flags = V8RuntimeOptions.V8_FLAGS;
        v8Flags.setMaxHeapSize(768);
        try (var v8Runtime = V8Host.getV8Instance().createV8Runtime()) {
            var globalObject = v8Runtime.getGlobalObject();
            globalObject.bind(fns);
            var javetStandardConsoleInterceptor = new JavetStandardConsoleInterceptor(v8Runtime);
            javetStandardConsoleInterceptor.register(globalObject);

            try {
                v8Runtime.getExecutor("""
                            var arr0 = [0, 1, 2];
                            console.log(arr0);
                            console.log(JSON.stringify(arr0));
                            var arr1 = getArray();
                            console.log(arr1);
                            console.log(JSON.stringify(arr1));
                            var arr2 = getList();
                            console.log(arr2);
                            console.log(JSON.stringify(arr2));
                        """).executeString();
            } finally {
                javetStandardConsoleInterceptor.unregister(globalObject);
                globalObject.unbind(fns);
                v8Runtime.lowMemoryNotification();
                globalObject.close();
            }
        }
    }
}
