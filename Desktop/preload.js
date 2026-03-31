const { contextBridge, ipcRenderer } = require('electron');

contextBridge.exposeInMainWorld('desktopShellAPI', {
  minimizeWindow: () => ipcRenderer.send('desktop:minimize'),
  closeWindow: () => ipcRenderer.send('desktop:close')
});