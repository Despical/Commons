package string;

import org.junit.Assert;
import org.junit.Test;

import me.despical.commonsbox.string.StringFormatUtils;

public class StringFormatUtilsTest {
	
	@Test
	public void formatIntoMMSS() {
		Assert.assertEquals("03:45", StringFormatUtils.formatIntoMMSS(3 * 60 + 45));
	}
}