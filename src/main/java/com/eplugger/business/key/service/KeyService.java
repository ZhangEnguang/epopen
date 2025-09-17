package com.eplugger.business.key.service;

import com.eplugger.business.key.model.Key;

public interface KeyService {
    Key generate();

    Boolean compare(Key key);
}
