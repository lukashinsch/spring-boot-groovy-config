info {
    mainFile = 'some value'
    conflict = 'main'
    dynamic = 1 + 2
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