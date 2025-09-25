package com.cy.rtspdemo.lisenter

/**
 * @desc 功能描述
 *
 * @author hudebo
 * @date 2023/12/26
 */
interface IPermissionListener {
    fun allGranted()
    fun denied(deniedList: List<String?>?)
}