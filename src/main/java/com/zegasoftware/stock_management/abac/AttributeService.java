//package com.zegasoftware.stock_management.abac;
//
//import com.zegasoftware.stock_management.model.dto.stock.StockDetails;
//import com.zegasoftware.stock_management.model.dto.user.UserDetails;
//import com.zegasoftware.stock_management.model.dto.user.UserSummary;
//import com.zegasoftware.stock_management.model.entity.Stock;
//import com.zegasoftware.stock_management.model.entity.User;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//import java.util.Map;
//
//@Component
//public class AttributeService {
//    public Map<String, Object> resourceAttributes(Object resource) {
//        if (resource instanceof Stock s) {
//            return Map.of(
//                    "sector", s.getSector(),
//                    "isDeleted", s.isDeleted()
//            );
//        }
//        if (resource instanceof StockDetails dto) {
//            return Map.of(
//                    "sector", dto.getSector(),
//                    "isDeleted", false    // or carry that flag in the DTO
//            );
//        }
//        if (resource instanceof User u) {
//            return Map.of(
//                    "sector", u.getSector(),
//                    "isDeleted", u.isDeleted()
//            );
//        }
//        if (resource instanceof UserSummary us) {
//            return Map.of(
//                    "sector", us.getSector(),
//                    "isDeleted", false
//            );
//        }
//        // …etc…
//        return Collections.emptyMap();
//    }
//}
