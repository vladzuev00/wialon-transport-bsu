//package by.bsu.wialontransport.protocol.core.server;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public final class ServerManager {
//    private final List<ProtocolServer<?, ?>> servers;
//
//    @PostConstruct
//    public void runServers() {
//        servers.stream()
//                .map(server -> new Thread(server::run))
//                .forEach(Thread::start);
//    }
//
//    @PreDestroy
//    public void stopServers() {
//        servers.forEach(ProtocolServer::stop);
//    }
//}
