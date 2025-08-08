#!/bin/bash
#
# 这个脚本会查找并杀死指定的SSH隧道进程。
# 关键在于，无论是否找到进程，它最后总会以“成功”(退出码0)的状态退出，
# 以防止 IntelliJ IDEA 报错并中断后续任务。

pkill -f "15432:localhost:5432"

exit 0