info {
    mainFile = 'some value'
    conflict = 'main'
}

spring {
    profiles {
        test {
            info {
                key = 'value'
            }
        }
        test2 {
            info {
                key2 = 'value2'
            }
        }
    }
}