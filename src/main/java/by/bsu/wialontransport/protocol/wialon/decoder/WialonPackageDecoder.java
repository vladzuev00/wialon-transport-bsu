package by.bsu.wialontransport.protocol.wialon.decoder;

//public class WialonPackageDecoder extends FixStringPrefixedPackageDecoder {
//
//    public WialonPackageDecoder(final String prefix) {
//        super(prefix);
//    }
//
//    @Override
//    public Package decode(ByteBuf buffer) {
//        return null;
//    }

//    @Override
//    public final WialonPackage decode(final String source) {
//        final String message = getMessage(source);
//        return decodeMessage(message);
//    }
//
//    protected abstract WialonPackage decodeMessage(final String message);
//
//    private String getMessage(final String source) {
//        final int startIndex = getPrefix().length();
//        final int nextEndIndex = source.length() - POSTFIX.length();
//        return source.substring(startIndex, nextEndIndex);
//    }
//}
