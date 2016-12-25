
import android.app.Activity;

import com.yatatsu.autobundle.AutoBundleField;

public class DuplicateKey extends Activity {

    @AutoBundleField(key = "key")
    String key1;

    @AutoBundleField(key = "key")
    String key2;
}
