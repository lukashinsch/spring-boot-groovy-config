info {
    mainFile = 'some value'
    conflict = 'main'
}

spring {
    profiles {
        test {
            info {
                key = 'test-value'
            }
        }
        test2 {
            info {
                key = 'test2-value'
            }
        }
    }
}