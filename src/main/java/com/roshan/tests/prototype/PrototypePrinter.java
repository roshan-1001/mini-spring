package com.roshan.tests.prototype;

import com.roshan.minispring.annotations.Scope;
import com.roshan.minispring.annotations.Service;
import com.roshan.minispring.enums.BeanScope;

@Service
@Scope(BeanScope.PROTOTYPE)
public class PrototypePrinter {
}
