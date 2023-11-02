//package by.bsu.wialontransport.protocol.wialon.decoder;
//
//import by.bsu.wialontransport.protocol.core.decoder.packages.PackageStringDecoder;
//import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;
//import lombok.RequiredArgsConstructor;
//
//import static by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage.POSTFIX;
//
//@RequiredArgsConstructor
//public abstract class WialonPackageDecoder<P extends WialonPackage> extends PackageStringDecoder<P> {
//    private final String packagePrefix;
//
//    @Override
//    public final boolean isAbleToDecode(final String source) {
//        return source.startsWith(this.packagePrefix);
//    }
//
//    @Override
//    public final P decode(final String source) {
//        final String message = this.extractMessage(source);
//        return this.decodeMessage(message);
//    }
//
//    protected abstract P decodeMessage(final String message);
//
//    private String extractMessage(final String source) {
//        final int startMessageIndex = this.packagePrefix.length();
//        final int endMessageNextIndex = source.length() - POSTFIX.length();
//        return source.substring(startMessageIndex, endMessageNextIndex);
//    }
//}
