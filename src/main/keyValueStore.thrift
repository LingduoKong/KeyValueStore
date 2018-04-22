namespace java apis.thrift.keyValueStore

service KeyValueStore {
    list<string>
    Get(1:string key),

    list<string>
    GetWithTime(1:string key, 2:i64 time),

    bool
    Put(1:string key, 2:string value),

    bool
    Delete(1:string key)

    bool
    DeleteWithValue(1:string key, 2:string value)

    list<string>
    Diff(1:string key, 2:i64 time1, 3:i64 time2)
}