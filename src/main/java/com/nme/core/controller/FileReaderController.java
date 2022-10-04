//package com.nme.core.controller;
//
//import com.google.cloud.ReadChannel;
//import com.google.cloud.storage.Storage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//import java.nio.ByteBuffer;
//
//@RestController
//public class FileReaderController {
//
//    @Autowired
//    private Storage storage;
//
//    @GetMapping(path = { "/hello" })
//    public Message readFromFile() throws IOException {
//
//        StringBuilder sb = new StringBuilder();
//
//        try (ReadChannel channel = storage.reader("icc-dev", "icc-data.json")) {
//            ByteBuffer bytes = ByteBuffer.allocate(64 * 1024);
//            while (channel.read(bytes) > 0) {
//                bytes.flip();
//                String data = new String(bytes.array(), 0, bytes.limit());
//                sb.append(data);
//                bytes.clear();
//            }
//        }
//
//        Message message = new Message();
//        message.setContents(sb.toString());
//
//        return message;
//    }
//}
//
//class Message {
//    private String contents;
//
//    public String getContents() {
//        return contents;
//    }
//
//    public void setContents(String contents) {
//        this.contents = contents;
//    }
//
//}
