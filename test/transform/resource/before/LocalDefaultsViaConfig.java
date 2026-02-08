//CONF: lombok.localDefaults.defaultFinal = true
//CONF: lombok.parameterDefaults.defaultFinal = true
class LocalDefaultsViaConfig {
	void normalMethod(String param, int count) {
		String local = "hello";
	}

	void withNonFinal(@lombok.experimental.NonFinal String param) {
		@lombok.experimental.NonFinal String local = "hello";
		local = "world";
		param = "changed";
	}

	void enhancedForLoop() {
		for (String item : java.util.Arrays.asList("a", "b")) {
			String inner = item;
		}
		for (@lombok.experimental.NonFinal int i = 0; i < 10; i++) {
		}
	}

	void tryCatch() {
		try {
			String x = "try";
		} catch (Exception e) {
			String y = "catch";
		}
	}

	LocalDefaultsViaConfig(String arg) {
		String local = arg;
	}
}
