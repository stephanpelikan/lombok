class LocalDefaultsViaConfig {
  void normalMethod(final String param, final int count) {
    final String local = "hello";
  }
  void withNonFinal(@lombok.experimental.NonFinal String param) {
    @lombok.experimental.NonFinal String local = "hello";
    local = "world";
    param = "changed";
  }
  void enhancedForLoop() {
    for (final String item : java.util.Arrays.asList("a", "b"))
      {
        final String inner = item;
      }
    for (@lombok.experimental.NonFinal int i = 0;; (i < 10); i ++)
      {
      }
  }
  void tryCatch() {
    try
      {
        final String x = "try";
      }
    catch (final Exception e)
      {
        final String y = "catch";
      }
  }
  LocalDefaultsViaConfig(final String arg) {
    super();
    final String local = arg;
  }
}
